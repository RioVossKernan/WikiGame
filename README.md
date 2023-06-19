# WikiGame

A program that uses calls to the wikipedia API to find the path from one wikipedia page to another using the fewest amount of clicks. 

My algorithm uses a Breadth-First-Search algorithm using forward links from the start page and back-links from the target page until there is a connection. 

It can reasonably find any page that is within three clicks of another (most are), further than that takes very long due to the exponential growth of wikipedia connections.
