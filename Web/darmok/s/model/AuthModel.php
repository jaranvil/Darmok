<?php
require_once '../model/User.php';
require_once '../model/data/UserDataModel.php';

class AuthModel 
{
    private $m_DataAccess;
    
    public function __construct()
    {
        $this->m_DataAccess = new UserDataModel();
    }
    
    public function __destruct()
    {
        // not doing anything at the moment
    }
            
    public function getToken($username, $password)
    {
        $this->m_DataAccess->connectToDB();                
        $token =  $this->m_DataAccess->getToken($username, $password);               
        $this->m_DataAccess->closeDB();
        
        return $token;
    }
    
    public function getUser($token)
    {
        $this->m_DataAccess->connectToDB();                
        $row =  $this->m_DataAccess->getUser($token);               
        $this->m_DataAccess->closeDB();
        
        $user = new User($row["username"], $row["email"], $row['name'], $row['color'], $row['level'], $row['xp']);
        
        return $user;
    }
    
    
//    
//    public function getCustomer($custID)
//    {
//        $this->m_DataAccess->connectToDB();
//        
//        $this->m_DataAccess->selectCustomerById($custID);
//        
//        $record =  $this->m_DataAccess->fetchCustomers();
//        
//        
//        $address = new Address(
//                 $this->m_DataAccess->fetchAddressID($record),
//                 $this->m_DataAccess->fetchAddress1($record),
//                 $this->m_DataAccess->fetchAddress2($record)
//                 );
//         $fetchedCustomer = new Customer($this->m_DataAccess->fetchCustomerID($record),
//                 $this->m_DataAccess->fetchCustomerFirstName($record),
//                 $this->m_DataAccess->fetchCustomerLastName($record),
//                 $address);
//            
//            
//        
//        $this->m_DataAccess->closeDB();
//        
//        return $fetchedCustomer;
//    }
//    
//     public function updateCustomer($customerToUpdate)
//    {
//        $this->m_DataAccess->connectToDB();
//        
//        $recordsAffected = $this->m_DataAccess->updateCustomer($customerToUpdate->getID(),
//                $customerToUpdate->getFirstName(),
//                $customerToUpdate->getLastName());
//        
//        return "$recordsAffected record(s) updated succesfully!";
//    }
}

