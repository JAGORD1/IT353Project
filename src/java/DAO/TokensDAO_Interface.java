/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author IT353S704
 */
public interface TokensDAO_Interface {
    public String submitToken(String id);
    public String verifyToken(String token);
}
