/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author sajid
 */
public class Log {
    
    private int ID;
    private Employee EmployeeID;
    private Inventory InventoryID;
    private String type;
    private Date date_logged;

     public Log() {
 
    }

    public Log(int ID, Employee EmployeeID, Inventory InventoryID, String type, Date date_logged) {
        this.ID = ID;
        this.EmployeeID = EmployeeID;
        this.InventoryID = InventoryID;
        this.type = type;
        this.date_logged = date_logged;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Employee getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(Employee EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public Inventory getInventoryID() {
        return InventoryID;
    }

    public void setInventoryID(Inventory InventoryID) {
        this.InventoryID = InventoryID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateLogged() {
        return date_logged;
    }

    public void setDateLogged(Date date_logged) {
        this.date_logged = date_logged;
    }
}
