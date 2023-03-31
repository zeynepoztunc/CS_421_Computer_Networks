# CS_421_Computer_Networks
This repository contains the homeworks that I did for Bilkent CS421 Computer Networks Course.
- `Homework 1` : Wireshark Assignment
   - `Description`:  In this homework assignment, we were asked to investigate application layer protocols such as HTTP and DNS by using packet analyzing sofwtare Wireshark. In HTTP, I analyzed basic HTTP get/response interactions, HTTP conditional get/response interactions, retriving long documents, HTML documents with embedded objects and HTTP authentication by examining the packets captured. I have examined the HTTP request/response messages, their contents, their status' and codes, the IP addresses of the destined messages and so on. In DNS, I have analyzed nslookup and ipconfig commands, and I have also used Wireshark for tracing DNS. I have examined the DNS queries, their contents, their types, the IP addresses of DNS servers. 
- `Programming Assignment 1` : Proxy Downloader
   - `Description`:  In this programming assignment, we were asked to develop a program in either Java or Python which downloads files using HTTP commands and implements a proxy server. In the first part of the assignment I have used Netcat working tool to analyze the HTTP request messages that a proxy server receives. In the second part, I have developed a program in Java which prints the HTTP "GET" messages that the browser(Firefox) is sending using a proxy server. The program retrieves the content of the requested file and it downloads it as long as user visits websites.
   -`How to run`: 
   1.  Compile: “javac ProxyDownloader.java”
   2.  Run: “java ProxyDownloader <port>” command.
