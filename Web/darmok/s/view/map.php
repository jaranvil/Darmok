
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>LandGrab</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" href="css/bootstrap.css" media="screen">
    <link rel="stylesheet" href="css/custom.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="../bower_components/html5shiv/dist/html5shiv.js"></script>
      <script src="../bower_components/respond/dest/respond.min.js"></script>
    <![endif]-->
<!--

    <script>

     var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-23019901-1']);
      _gaq.push(['_setDomainName', "bootswatch.com"]);
        _gaq.push(['_setAllowLinker', true]);
      _gaq.push(['_trackPageview']);

     (function() {
       var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
       ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
     })();

    </script>
-->
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  </head>
  <body style="height:100%;">
    <div class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a href="../" class="navbar-brand">LandGrab</a>
          <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
          <ul class="nav navbar-nav">           
            <li>
              <a href="" class="active">Map</a>
            </li>
            <li>
              <a href="" class="active">Account</a>
            </li>
              
            
          </ul>

          <ul class="nav navbar-nav navbar-right">
            <li><a href="" onclick="logOut();">Logout</a></li>            
          </ul>

        </div>
      </div>
    </div>

      <div id="cloud-container">
        
<!--        <div class="hexagon"></div>-->


          
        <div class="row" style="margin-top:52px;">
            <div class="col-sm-4">
                
                <div class="level_box">
                    <?php echo $user->getLevel(); ?>
                </div>
                <div class="user_info">
                    <h3 style="margin-bottom:0px"><?php  echo $user->getName();  ?></h3>
                    <span style="color:orange"><?php echo $user->getUsername(); ?></span>
                </div>
                
            </div>
            <div class="col-sm-12">
                
                <div class="progress progress-striped">
                    <div class="progress-bar progress-bar-warning" style="width: 60%">
                        <span>600/1000</span>
                    </div>
                  </div>
            </div>  
        </div>
            <div class="map_options">
                <ul class="nav nav-pills">
                  <li class="active"><a href="">All Users</a></li>
                  <li><a href="">Your Land</a></li>
                  
                </ul>  
          </div>
          
        <div id="map" style="height:100%;width:100%;"></div> 
      </div>


     

<!--
      <footer>
        <div class="row">
          <div class="col-lg-12">

            <ul class="list-unstyled">
              <li class="pull-right"><a href="#top">Back to top</a></li>
              <li><a href="http://news.bootswatch.com" onclick="pageTracker._link(this.href); return false;">Blog</a></li>
              <li><a href="http://feeds.feedburner.com/bootswatch">RSS</a></li>
              <li><a href="https://twitter.com/bootswatch">Twitter</a></li>
              <li><a href="https://github.com/thomaspark/bootswatch/">GitHub</a></li>
              <li><a href="../help/#api">API</a></li>
              <li><a href="../help/#support">Support</a></li>
            </ul>
            <p>Made by <a href="http://thomaspark.co" rel="nofollow">Thomas Park</a>. Contact him at <a href="/cdn-cgi/l/email-protection#8bffe3e4e6eaf8cbe9e4e4fff8fceaffe8e3a5e8e4e6"><span class="__cf_email__" data-cfemail="c9bda1a6a4a8ba89aba6a6bdbabea8bdaaa1e7aaa6a4">[email&#160;protected]</span><script data-cfhash='f9e31' type="text/javascript">/* <![CDATA[ */!function(t,e,r,n,c,a,p){try{t=document.currentScript||function(){for(t=document.getElementsByTagName('script'),e=t.length;e--;)if(t[e].getAttribute('data-cfhash'))return t[e]}();if(t&&(c=t.previousSibling)){p=t.parentNode;if(a=c.getAttribute('data-cfemail')){for(e='',r='0x'+a.substr(0,2)|0,n=2;a.length-n;n+=2)e+='%'+('0'+('0x'+a.substr(n,2)^r).toString(16)).slice(-2);p.replaceChild(document.createTextNode(decodeURIComponent(e)),c)}p.removeChild(t)}}catch(u){}}()/* ]]> */</script></a>.</p>
            <p>Code released under the <a href="https://github.com/thomaspark/bootswatch/blob/gh-pages/LICENSE">MIT License</a>.</p>
            <p>Based on <a href="http://getbootstrap.com" rel="nofollow">Bootstrap</a>. Icons from <a href="http://fortawesome.github.io/Font-Awesome/" rel="nofollow">Font Awesome</a>. Web fonts from <a href="http://www.google.com/webfonts" rel="nofollow">Google</a>.</p>

          </div>
        </div>

      </footer>
-->

      <script>
        function logOut()
        {
          $.post("../public/index.php", { logout: "logout"} );
       }
      </script>
      
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="../assets/js/custom.js"></script>
      
    <script src="js/infobubble.js"></script>
      
    <script src="js/map_script.js"></script>
    
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA_oZMVy8xckikqAFOoueG455dUkzz8vaA&callback=initMap"
        async defer></script>
      
  <script type="text/javascript">/* <![CDATA[ */(function(d,s,a,i,j,r,l,m,t){try{l=d.getElementsByTagName('a');t=d.createElement('textarea');for(i=0;l.length-i;i++){try{a=l[i].href;s=a.indexOf('/cdn-cgi/l/email-protection');m=a.length;if(a&&s>-1&&m>28){j=28+s;s='';if(j<m){r='0x'+a.substr(j,2)|0;for(j+=2;j<m&&a.charAt(j)!='X';j+=2)s+='%'+('0'+('0x'+a.substr(j,2)^r).toString(16)).slice(-2);j++;s=decodeURIComponent(s)+a.substr(j,m-j)}t.innerHTML=s.replace(/</g,'&lt;').replace(/>/g,'&gt;');l[i].href='mailto:'+t.value}}catch(e){}}}catch(e){}})(document);/* ]]> */</script></body>
</html>
