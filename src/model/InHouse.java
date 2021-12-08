package model;

/**This class extends abstract class.
 * Naming conventions are
 * identical to the provided
 * UML diagram.
 * Auto-generated using built-in
 * auto-generate code in IntelliJ
 */
public class InHouse extends Part{
    private int machineID;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**Use constructor from separate class
     * to generate ID Number
     * @return generates ID
     */
    public int getMachineID() {
        return machineID;
}
}
