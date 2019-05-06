/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.db;

/**
 *
 * @author 20150223
 */
public class MMTDBException extends Exception {

    /**
     * Creates a new instance of <code>MMTDBException</code> without detail
     * message.
     */
    public MMTDBException() {
    }

    /**
     * Constructs an instance of <code>MMTDBException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public MMTDBException(String msg) {
        super(msg);
    }
}
