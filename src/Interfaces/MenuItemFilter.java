package Interfaces;

import Models.MenuItem;


@FunctionalInterface
public interface MenuItemFilter {
    boolean test(MenuItem item);
}