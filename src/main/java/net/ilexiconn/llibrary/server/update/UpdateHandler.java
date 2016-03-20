package net.ilexiconn.llibrary.server.update;

import com.google.gson.Gson;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import java.util.ArrayList;
import java.util.List;

public enum UpdateHandler {
    INSTANCE;

    private List<UpdateContainer> updateContainerList = new ArrayList<>();
    private List<UpdateContainer> outdatedModList = new ArrayList<>();

    public void registerUpdateChecker(Object mod, String url) {
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            LLibrary.LOGGER.warn("Please register the update checker using the main mod class. Skipping registeration of object " + mod + ".");
            return;
        }

        UpdateContainer updateContainer = new Gson().fromJson(WebUtils.readURL(url), UpdateContainer.class);
        Mod annotaton = mod.getClass().getAnnotation(Mod.class);

        final ModContainer[] modContainer = {null};
        Loader.instance().getModList().stream().filter(container -> container.getModId().equals(annotaton.modid())).forEach(container -> modContainer[0] = container);

        if (modContainer[0] == null) {
            LLibrary.LOGGER.warn("Cound't find mod container with id " + annotaton.modid() + ". Skipping registeration of object " + mod + ".");
            return;
        }

        updateContainer.setModContainer(modContainer[0]);
        updateContainer.setIcon(WebUtils.downloadImage(updateContainer.getIconURL()));

        this.updateContainerList.add(updateContainer);
    }

    public void searchForUpdates() {
        this.updateContainerList.stream().filter(updateContainer -> updateContainer.getLatestVersion().compareTo(updateContainer.getModContainer().getProcessedVersion()) > 0).forEach(updateContainer -> outdatedModList.add(updateContainer));
    }

    public List<UpdateContainer> getOutdatedModList() {
        return outdatedModList;
    }

    public String[] getChangelog(UpdateContainer updateContainer, ArtifactVersion version) {
        if (hasChangelog(updateContainer, version)) {
            return updateContainer.getVersions().get(version.getVersionString());
        } else {
            return new String[]{};
        }
    }

    public boolean hasChangelog(UpdateContainer updateContainer, ArtifactVersion version) {
        return updateContainer.getVersions().containsKey(version.getVersionString());
    }
}
