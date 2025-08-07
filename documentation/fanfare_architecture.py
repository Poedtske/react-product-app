from diagrams import Cluster, Diagram
from diagrams.aws.compute import ECS
from diagrams.aws.network import ClientVpn
from diagrams.onprem.client import Users
from diagrams.onprem.database import MySQL
from diagrams.programming.framework import Spring
from diagrams.programming.language import Python
from diagrams.generic.os import Debian
from diagrams.custom import Custom
from diagrams.onprem.network import Nginx

with Diagram("Fanfare Architecture", show=True, direction="TB"):
    with Cluster("OVH Debian Server"):
        with Cluster("Docker Compose Stack"):
            # server = Debian()
            springboot = Spring()
            scraper = Python("Spond API")
            frontend = Custom("", "./react_logo.png")
            mysql = MySQL()
            Nginx = Nginx("Reverse Proxy")

            # Inter-service communication
            frontend - springboot
            scraper - springboot
            springboot - mysql
            Nginx - frontend
            Nginx - springboot
