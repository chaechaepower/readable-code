package cleancode.studycafe.me.model;

public class PassOrder {
    private final StudyCafePass pass;
    private final StudyCafeLockerPass lockerPass;

    private PassOrder(StudyCafePass pass, StudyCafeLockerPass lockerPass) {
        this.pass = pass;
        this.lockerPass = lockerPass;
    }

    public static PassOrder of(StudyCafePass pass, StudyCafeLockerPass lockerPass) {
        return new PassOrder(pass, lockerPass);
    }

    public int calculateDiscountPrice() {
        return (int) (pass.getPrice() * pass.getDiscountRate());
    }

    public int calculateTotalPrice() {
        int discountPrice = calculateDiscountPrice();
        int lockerPassPrice = getLockerPassPrice();
        return pass.getPrice() - discountPrice + lockerPassPrice;
    }

    public int getLockerPassPrice() {
        return lockerPass != null ? lockerPass.getPrice() : 0;
    }

    public String getPassInfo() {
        return pass.display();
    }

    public String getLockerPassInfo() {
        return lockerPass.display();
    }

    public boolean isLockerPassPresent() {
        return lockerPass != null;
    }
}
