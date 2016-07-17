<?php



class Location
{
    private $lat;
    private $lng;
    
    public function __construct($lat, $lng)
    {
        $this->lat = $lat;
        $this->lng = $lng;
    }
    
    public function getLat()
    {
        return ($this->lat);
    }
    
    public function getLng()
    {
        return ($this->lng);
    }
    
    public function setLat($lat)
    {
        $this->lat = $lat;
    }

    public function setLng()
    {
        return ($this->lng);
    }
    
  
}

?>
