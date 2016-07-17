<?php

require_once '../model/Customer.php';
require_once '../model/Address.php';

//require_once '../model/data/MySQLiCustomerDataModel.php';
require_once '../model/data/PDOMySQLCustomerDataModel.php';

class DashboardModel 
{
    private $m_DataAccess;
    
    public function __construct()
    {
        //$this->m_DataAccess = new MySQLiCustomerDataModel();
        $this->m_DataAccess = new PDOMySQLCustomerDataModel();
    }
    
    public function __destruct()
    {
        // not doing anything at the moment
    }
            
    public function getAllCustomers()
    {
        $this->m_DataAccess->connectToDB();
        
        $arrayOfCustomerObjects = array();
        
        $this->m_DataAccess->selectCustomers();
        
        while($row =  $this->m_DataAccess->fetchCustomers())
        {
           $address = new Address(
                    $this->m_DataAccess->fetchAddressID($row),
                    $this->m_DataAccess->fetchAddress1($row),
                    $this->m_DataAccess->fetchAddress2($row)
                    );
            $currentCustomer = new Customer($this->m_DataAccess->fetchCustomerID($row),
                    $this->m_DataAccess->fetchCustomerFirstName($row),
                    $this->m_DataAccess->fetchCustomerLastName($row),
                    $address);
            
            $arrayOfCustomerObjects[] = $currentCustomer;
        }
        
        $this->m_DataAccess->closeDB();
        
        return $arrayOfCustomerObjects;
    }
    
    public function getCustomer($custID)
    {
        $this->m_DataAccess->connectToDB();
        
        $this->m_DataAccess->selectCustomerById($custID);
        
        $record =  $this->m_DataAccess->fetchCustomers();
        
        
        $address = new Address(
                 $this->m_DataAccess->fetchAddressID($record),
                 $this->m_DataAccess->fetchAddress1($record),
                 $this->m_DataAccess->fetchAddress2($record)
                 );
         $fetchedCustomer = new Customer($this->m_DataAccess->fetchCustomerID($record),
                 $this->m_DataAccess->fetchCustomerFirstName($record),
                 $this->m_DataAccess->fetchCustomerLastName($record),
                 $address);
            
            
        
        $this->m_DataAccess->closeDB();
        
        return $fetchedCustomer;
    }
    
     public function updateCustomer($customerToUpdate)
    {
        $this->m_DataAccess->connectToDB();
        
        $recordsAffected = $this->m_DataAccess->updateCustomer($customerToUpdate->getID(),
                $customerToUpdate->getFirstName(),
                $customerToUpdate->getLastName());
        
        return "$recordsAffected record(s) updated succesfully!";
    }
}

?>
