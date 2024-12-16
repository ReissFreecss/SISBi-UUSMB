/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jsf;

/**
 *
 * @author lucio
 */
import javax.ejb.Stateless;  
  
@Stateless(mappedName="st1")  
public class AdderImpl implements AdderImplRemote {  
  public int add(int a,int b){  
      return a+b;  
  }  
}  