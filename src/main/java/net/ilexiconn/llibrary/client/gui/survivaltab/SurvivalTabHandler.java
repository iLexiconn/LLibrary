package net.ilexiconn.llibrary.client.gui.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public enum SurvivalTabHandler {
    INSTANCE;

    private List<SurvivalTab> survivalTabList = new ArrayList<>();
    private int currentPage;

    public SurvivalTab create(String label) {
        SurvivalTab survivalTab = new SurvivalTab(this.survivalTabList.size(), label);
        this.survivalTabList.add(survivalTab);
        return survivalTab;
    }

    public List<SurvivalTab> getSurvivalTabList() {
        return this.survivalTabList;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int page) {
       this.currentPage = page;
    }
}
