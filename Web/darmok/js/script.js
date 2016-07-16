var centerLat = 44.648,
    centerLng = -63.589,
    lastLoadLat = 0,
    lastLoadLng = 0;
    map,
    cellSize = 0.001,
    gridLines = Array(),
    cellObjs = Array(),
    infoWindow = Array(),
    cellArr = Array(),
    supportArr = Array();


function drawCells()
{
    // clear cells
    for (var i = 0;i < cellObjs.length;i++)
    {
        cellObjs[i].setMap(null);        
    }
    cellObjs = [];
    
    // for each cell
    for (var i = 0;i < cellArr.length;i++)
    {
        // get cell supporters
        var cellSupporters = Array();
        for (var c = 0;c < supportArr.length;c++)
        {
            if (cellArr[i][0] == supportArr[c][0]) // cell_id == cell_id
            {
                cellSupporters.push(supportArr[c]);
            }
        }
        
        // find owner
        var ownerIndex = 0;
        for (var c = 0;c < cellSupporters.length;c++)
        {
            if (cellSupporters[c][1] > cellSupporters[ownerIndex][1])
                ownerIndex = c;
        }
        
        var color = '#ffffff';
        if (cellSupporters[ownerIndex][2] == 'Blue')
            color = '#0000ff';
        if (cellSupporters[ownerIndex][2] == 'Red')
            color = '#ff0000';
        if (cellSupporters[ownerIndex][2] == 'Green')
            color = '#00ff00';
        if (cellSupporters[ownerIndex][2] == 'Yellow')
            color = '#ffff00';
        
        var cell = new google.maps.Rectangle({
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 0,
            fillColor: color,
            fillOpacity: 0.2,                   
            bounds: {
              north: Number(cellArr[i][1]),
              south: Number(cellArr[i][1]) + Number(cellSize),
              east: Number(cellArr[i][2]) + Number(cellSize),
              west: Number(cellArr[i][2])
            }
          });

        cell.addListener('click', showCellInfo);

        //infoWindow = new google.maps.InfoWindow;
        
        cellObjs.push(cell);        
    }
    
    for (var i = 0;i < cellObjs.length;i++)
    {
        cellObjs[i].setMap(map);
    }
}

function trimNumber(num)
{
    var numString = num.toString();    
    var charArr = numString.split('');
    var resultStirng = "";
    var decimal = 0;
    for (var i = 0;i < charArr.length;i++)
    {
        if (charArr[i] == '.')
        {
            decimal = i;
        }
        if (i < decimal + 4)
        {
            resultStirng += charArr[i];
        }        
    }
    
    return Number(resultStirng);
}

function showCellInfo(event)
{    
    // close old windows
    for (var i = 0;i < infoWindow.length;i++)
    {
        infoWindow[i].close();        
    }            
    
    // ***** toFixed is rounding not triming!
    
    // get support list for current cell
    var clickLat = trimNumber(event.latLng.lat());
    var clickLng = trimNumber(event.latLng.lng());
    var content = "<div style='color:#ffffff'>";
    
    alert(event.latLng.lat() + ", " + event.latLng.lng() + " : " + clickLat + ", " + clickLng);
    
    var supporters = Array();
    
    supporters = getCellSupporters(clickLat, clickLng);
    
   for (var i = 0;i < supporters.length;i++)
   {
       content += supporters[i][6] + "<p>";
   }
    
    content += "</div>";
    
   infoWindow.push(
       new InfoBubble({
          map: map,
          content: content,
          position: event.latLng,
          shadowStyle: 1,
          padding: 5,
          backgroundColor: '#444444',
          borderRadius: 5,
          arrowSize: 20,
          borderWidth: 1,
          borderColor: '#ffff00',
          disableAutoPan: true,
          hideCloseButton: false,
          arrowPosition: 30,
          backgroundClassName: 'transparent',
          arrowStyle: 2,
          minWidth: 200,
          minHeight: 250,
          disableAnimation: true
        })
   ); 

    for (var i = 0;i < infoWindow.length;i++)
    {
        infoWindow[infoWindow.length - 1].open();        
    }
    
  // Replace the info window's content and position.
//  infoWindow.setContent("<div style='background-color:#000000;color:#ffffff;width:100%;height:100%;'>test<p>test2</div>");
//  infoWindow.setPosition(event.latLng);
//
//  infoWindow.open(map);
}

function getCellSupporters(lat, lng)
{
    // for each cell
    for (var i = 0;i < cellArr.length;i++)
    {
        // find cell
        if (cellArr[i][1] == lat && cellArr[i][2] == lng)
        {
            // get cell supporters
            var cellSupporters = Array();
            for (var c = 0;c < supportArr.length;c++)
            {
                if (cellArr[i][0] == supportArr[c][0]) // cell_id == cell_id
                {
                    cellSupporters.push(supportArr[c]);
                }
            }
            return cellSupporters;
        }               
    }
}

function loadCells()
{
    lastLoadLat = map.getCenter().lat();
    lastLoadLng = map.getCenter().lng();
    
    cellArr = [];
    supportArr = [];
    
     $.ajax({
      type: 'POST',
      url: 'http://jaredeverett.ca/darmok/web_api/getCellsInArea.php',
      data: { 
        'lat': centerLat, 
        'lng': centerLng
      },
      async: false,
      dataType: 'json',
      success: function (data) {
         
        var cells = data['cell'];
        for (var i = 0;i < data['cell'].length;i++)
        {
            cellArr.push([cells[i].id, cells[i].lat, cells[i].lng]);
        }     
            
        var supporters = data['support'];
        for (var i = 0;i < data['support'].length;i++)
        {
            supportArr.push([supporters[i].cell_id, supporters[i].amount, supporters[i].color, supporters[i].lat, supporters[i].lng, supporters[i].name, supporters[i].username]);
        }     
    
      }
    });

    drawCells();
}

function initMap() 
{
    
   
   // alert(token);
    
      // Specify features and elements to define styles.
//      var styleArray = 
//                 [{"featureType":"landscape","stylers":[{"saturation":-100},{"lightness":65},{"visibility":"on"}]},{"featureType":"poi","stylers":[{"saturation":-100},{"lightness":51},{"visibility":"simplified"}]},{"featureType":"road.highway","stylers":[{"saturation":-100},{"visibility":"simplified"}]},{"featureType":"road.arterial","stylers":[{"saturation":-100},{"lightness":30},{"visibility":"on"}]},{"featureType":"road.local","stylers":[{"saturation":-100},{"lightness":40},{"visibility":"on"}]},{"featureType":"transit","stylers":[{"saturation":-100},{"visibility":"simplified"}]},{"featureType":"administrative.province","stylers":[{"visibility":"off"}]},{"featureType":"water","elementType":"labels","stylers":[{"visibility":"on"},{"lightness":-25},{"saturation":-100}]},{"featureType":"water","elementType":"geometry","stylers":[{"hue":"#ffff00"},{"lightness":-25},{"saturation":-97}]}]
      
      
      var styleArray = 
      [{"featureType":"all","elementType":"labels.text.fill","stylers":[{"saturation":36},{"color":"#000000"},{"lightness":40}]},{"featureType":"all","elementType":"labels.text.stroke","stylers":[{"visibility":"on"},{"color":"#000000"},{"lightness":16}]},{"featureType":"all","elementType":"labels.icon","stylers":[{"visibility":"off"}]},{"featureType":"administrative","elementType":"geometry.fill","stylers":[{"color":"#000000"},{"lightness":20}]},{"featureType":"administrative","elementType":"geometry.stroke","stylers":[{"color":"#000000"},{"lightness":17},{"weight":1.2}]},{"featureType":"landscape","elementType":"geometry","stylers":[{"color":"#000000"},{"lightness":20}]},{"featureType":"poi","elementType":"geometry","stylers":[{"color":"#000000"},{"lightness":21}]},{"featureType":"road.highway","elementType":"geometry.fill","stylers":[{"color":"#000000"},{"lightness":17}]},{"featureType":"road.highway","elementType":"geometry.stroke","stylers":[{"color":"#000000"},{"lightness":29},{"weight":0.2}]},{"featureType":"road.arterial","elementType":"geometry","stylers":[{"color":"#000000"},{"lightness":18}]},{"featureType":"road.local","elementType":"geometry","stylers":[{"color":"#000000"},{"lightness":16}]},{"featureType":"transit","elementType":"geometry","stylers":[{"color":"#000000"},{"lightness":19}]},{"featureType":"water","elementType":"geometry","stylers":[{"color":"#000000"},{"lightness":17}]}]
      

    // Create a map object and specify the DOM element for display.
      map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: centerLat, lng: centerLng},
        scrollwheel: true,
        // Apply the map style array to the map.
        styles: styleArray,
        disableDefaultUI: true,
        zoom: 14
      });
    
  
    // markers
//            var link = "http://jaredeverett.ca/dropchat/marker_link.png";
//            var text = "http://jaredeverett.ca/dropchat/marker_text.png";
//            var photo = "http://jaredeverett.ca/dropchat/marker_photo.png";
//            $.ajax({
//                type: "POST",
//                url: "getMarkers.php",
//                dataType: 'json', 
//                async: false, 
//                success: function(j) {		                    
//                    var count = j.photo.length;
//                    for (var i = 0;i < count; i++)
//                    {
//                        var pos = {lat: Number(j.photo[i].lat), lng: Number(j.photo[i].lng)},
//                            icon;
//                        if (j.photo[i].type == "1")
//                            icon = text;
//                        if (j.photo[i].type == "2")
//                            icon = link;
//                        if (j.photo[i].type == "3")
//                            icon = photo;
//                        
//                        var marker = new google.maps.Marker({
//                          position: pos,                   
//                          map: map,
//                          icon: icon,
//                          title: j.photo[i].title
//                        });
//                    }
//                
//                }
//            });


    // draw gridlines when bounds of map change

    loadCells();
    
    map.addListener('bounds_changed', function() 
    {
        // get center of map
        centerLat = map.getCenter().lat().toFixed(3);
        centerLng = map.getCenter().lng().toFixed(3);
                
        // load data if the map has moved a lot since last load
        var latDiff = Math.abs(centerLat - lastLoadLat);
        var lngDiff = Math.abs(centerLng - lastLoadLng);
        if (latDiff > 0.05 || lngDiff > 0.05)
            loadCells();
        
        // clear out lines
        gridLines.forEach(function(line) {
            line.setMap(null);
        });
        gridLines = [];

        if (map.getZoom() > 13)
        {
            var bounds = map.getBounds();
        // Horizontal lines

        var numHLines = Math.abs((bounds.getSouthWest().lat() - bounds.getNorthEast().lat()) / cellSize);

        var topLat = bounds.getSouthWest().lat();
        var startLat = Number(topLat.toFixed(3));

        for (var i = 0;i < numHLines;i++)
        {
            var linePath = [
                {lat: startLat + (cellSize * i), lng: bounds.getSouthWest().lng()},
                {lat: startLat + (cellSize * i), lng: bounds.getNorthEast().lng()}
              ];

            var line = new google.maps.Polyline({
                path: linePath,
                geodesic: true,
                strokeColor: '#ffffff',
                strokeOpacity: 0.2,
                strokeWeight: 1
              });

            gridLines.push(line);
        }

        // Vertical lines

        var numVLines = Math.abs((bounds.getSouthWest().lng() - bounds.getNorthEast().lng()) / cellSize);

        var leftLng = bounds.getSouthWest().lng();
        var startLng = Number(leftLng.toFixed(3));

        for (var i = 0;i < numVLines;i++)
        {
            var linePath = [
                {lat: bounds.getSouthWest().lat(), lng: startLng + (cellSize * i)},
                {lat: bounds.getNorthEast().lat(), lng: startLng + (cellSize * i)}
              ];

            var line = new google.maps.Polyline({
                path: linePath,
                geodesic: true,
                strokeColor: '#ffffff',
                strokeOpacity: 0.2,
                strokeWeight: 1
              });

            gridLines.push(line);
        }

        // draw lines

        gridLines.forEach(function(line) {
            line.setMap(map);
        });

        }


      });

    }
