/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jsf;

import javax.ejb.Remote;  

@Remote  
public interface AdderImplRemote {  
int add(int a,int b);  
}  