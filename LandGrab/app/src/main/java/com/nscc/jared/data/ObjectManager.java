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
    public int level;
    public int xp;
    public int playerPower = 0;
    public ArrayList<Cell> cells = new ArrayList<>();
    public ArrayList<CellSupport> cellSupporters = new ArrayList<>();

    public void login(String user, String pass)
    {
        dbUser.login(user, pass);
        waitingForLoginToLoad = true;
    }

    public void addXP()
    {

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

    public void loadMapData(double lat, double lng, String username)
    {
        dbCell.loadCellsInArea(lat, lng);
        dbUser.getStats(username);
        waitingForCellsToLoad = true;
    }

    public boolean loadComplete()
    {
        if (dbCell.loadCellsComplete && dbUser.powerCompletebool)
        {

            cells = dbCell.cells;
            cellSupporters = dbCell.supports;
            playerPower = dbUser.power;
            level = dbUser.level;
            xp = dbUser.xp;

            waitingForCellsToLoad = false;
            dbCell.loadCellsComplete = false;
            dbUser.powerCompletebool = false;
            return true;
        }
        else
        {
            return false;
        }
    }

}
