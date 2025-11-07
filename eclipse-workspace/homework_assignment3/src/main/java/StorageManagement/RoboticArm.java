package StorageManagement;

public class RoboticArm {

    private boolean active;

    public void moveToStorage() {
        activate();
        System.out.println("Robotic Arm: Moving to storage...");
        deactivate();
    }

    public void activate() {
        active = true;
        System.out.println("Robotic Arm: Activated.");
    }

    public void deactivate() {
        active = false;
        System.out.println("Robotic Arm: Deactivated.");
    }

    public boolean isActive() {
        return active;
    }

	public void setActive(boolean active) {
		this.active = active;
	}

	
}
