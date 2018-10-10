/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.noip.fideo.habitisync;

import java.util.Collection;

/**
 *
 * @author fran
 */
public interface Resource {
    public Collection<Task> getTasks() throws Exception;

    public void create(Task task);
}
