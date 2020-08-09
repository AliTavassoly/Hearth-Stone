package hearthstone.models.behaviours;

public interface Upgradeable {
    boolean isUpgraded();
    void setUpgraded(boolean upgraded);
    void updateUpgraded();
}
