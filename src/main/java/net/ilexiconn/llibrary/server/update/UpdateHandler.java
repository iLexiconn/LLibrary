package net.ilexiconn.llibrary.server.update;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Update checker handler. Based off FiskFille's version for LLibrary Beta.
 *
 * @author iLexiconn
 * @since 1.0.0
 */
public enum UpdateHandler {
    INSTANCE;

    private List<UpdateContainer> updateContainerList = new ArrayList<>();
    private List<UpdateContainer> outdatedModList = new ArrayList<>();

    /**
     * Register a update checker. The first object has to be the main mod class - the class with the
     * {@link net.minecraftforge.fml.common.Mod} annotation. The second argument is the url to the version checker.
     * <p>Example:
     * <pre>{@code
     * {
     *  "version": "1.0.0",
     *  "updateURL": "http://www.planetminecraft.com/mod/llibrary/",
     *  "iconURL": "http://i.imgur.com/G7NjFTR.png",
     *  "versions": {
     *     "1.0.0": [
     *       "Initial release"
     *     ]
     *   }
     * }
     * }
     * </pre>
     *
     * @param mod the main mod instance
     * @param url the json url
     */
    public void registerUpdateChecker(Object mod, String url) {
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            LLibrary.LOGGER.warn("Please register the update checker using the main mod class. Skipping registration of object " + mod + ".");
            return;
        }

        Mod annotation = mod.getClass().getAnnotation(Mod.class);
        try {
            UpdateContainer updateContainer = new Gson().fromJson(WebUtils.readURL(url), UpdateContainer.class);
            if (updateContainer != null) {
                final ModContainer[] modContainer = {null};
                Loader.instance().getModList().stream().filter(container -> container.getModId().equals(annotation.modid())).forEach(container -> modContainer[0] = container);

                if (modContainer[0] == null) {
                    LLibrary.LOGGER.warn("Couldn't find mod container with id " + annotation.modid() + ". Skipping registration of object " + mod + ".");
                    return;
                }

                updateContainer.setModContainer(modContainer[0]);
                if (!updateContainer.getIconURL().isEmpty()) {
                    updateContainer.setIcon(WebUtils.downloadImage(updateContainer.getIconURL()));
                }

                this.updateContainerList.add(updateContainer);
            } else {
                LLibrary.LOGGER.warn("Failed to load update container for mod " + annotation.name() + " (" + annotation.modid() + ")!");
            }
        } catch (JsonSyntaxException e) {
            LLibrary.LOGGER.warn("Failed to load update container for mod " + annotation.name() + " (" + annotation.modid() + ")!");
        }
    }

    /**
     * Search for mod updates. This method is getting called by {@link ServerProxy#onPostInit()}
     */
    public void searchForUpdates() {
        this.updateContainerList.stream().filter(updateContainer -> updateContainer.getLatestVersion().compareTo(updateContainer.getModContainer().getProcessedVersion()) > 0).forEach(updateContainer -> outdatedModList.add(updateContainer));
    }

    /**
     * @return a list of all outdated mod containers.
     */
    public List<UpdateContainer> getOutdatedModList() {
        return outdatedModList;
    }

    /**
     * Get the changelog for a specefic version. Never returns null.
     *
     * @param updateContainer the mod container
     * @param version         the version
     * @return the changelog for a specefic version
     */
    public String[] getChangelog(UpdateContainer updateContainer, ArtifactVersion version) {
        if (hasChangelog(updateContainer, version)) {
            return updateContainer.getVersions().get(version.getVersionString());
        } else {
            return new String[]{};
        }
    }

    /**
     * Check if LLibrary has a changelog for a specific mod version.
     *
     * @param updateContainer the mod container
     * @param version         the version
     * @return true if LLibrary has a changelog for the version
     */
    public boolean hasChangelog(UpdateContainer updateContainer, ArtifactVersion version) {
        return updateContainer.getVersions().containsKey(version.getVersionString());
    }
}
