/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.stilo.g.structures;

/*
 * #%L
 * G-github
 * %%
 * Copyright (C) 2013 - 2016 Giovanni Stilo
 * %%
 * G is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/lgpl-3.0.txt>.
 * #L%
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author stilo
 */
public class ObjectValues implements Comparable {

    private static final Logger logger = LogManager.getLogger(ObjectValues.class);

    public Object term;
    public int value;
    
    public ObjectValues(Object term, int  value) {
        this.term = term;
        this.value = value;       
    }

    @Override
    public boolean equals(Object o) {
        return (value == ((ObjectValues) o).value);
    }

    public int compareTo(Object o) {
        return Integer.compare(((ObjectValues) o).value,value);        
    }

}
