/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author sajid
 */
public interface Subject {
        void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(String action, Employee employee, Inventory inventory, Date date);
}
