package com.nscc.jared.data;


import com.nscc.jared.biz.Cell;
import com.nscc.jared.biz.CellSupport;

import java.io.Serializable;
import java.util.ArrayList;

public class ObjectManager
{

    CellDataAccess dbCell = new CellDataAccess();
    public UserDataAccess dbUser = new UserDataAccess();

    public boolean waitingForLoginToLoad = false;
    public boolean waitingForCellsToLoad = false;

    // data
    public int user_id;
    public ArrayList<Cell> cells = new ArrayList<>();
    public ArrayList<CellSupport> cellSupporters = new ArrayList<>();

    public void login(String user, String pass)
    {
        dbUser.login(user, pass);
        waitingForLoginToLoad = true;
    }

    public boolean loginComplete()
    {
        if (dbUser.loginComplete)
        {
            user_id = dbUser.id;

            waitingForLoginToLoad = false;
            dbUser.loginComplete = false;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void loadCells(double lat, double lng)
    {
        dbCell.loadCellsInArea(lat, lng);
        waitingForCellsToLoad = true;
    }

    public boolean loadComplete()
    {
        if (dbCell.loadCellsComplete)
        {

            cells = dbCell.cells;
            cellSupporters = dbCell.supports;

            waitingForCellsToLoad = false;
            dbCell.loadCellsComplete = false;
            return true;
        }
        else
        {
            return false;
        }
    }

}
