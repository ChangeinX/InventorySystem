package model;

/**Extends abstract class
 * Naming conventions are
 * identical to the provided
 * UML diagram. Auto-generated using built-in
 * auto-generate code in IntelliJ
 */

public class OutSourced extends Part{
    private String companyName;
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    public String getCompanyName() {
        return companyName;
    }
}
