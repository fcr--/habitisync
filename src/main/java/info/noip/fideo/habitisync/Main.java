/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.noip.fideo.habitisync;

/**
 *
 * @author fran
 */
public class Main {

    public static void main(String args[]) throws Exception {
        System.out.println("hola!");
        HabiticaResource hres = new HabiticaResource("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx", "yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy");
        hres.getTasks();
//        GCalResource gcal = new GCalResource();
//        update(new Resource[]{hres, gcal}, db);
    }
}
