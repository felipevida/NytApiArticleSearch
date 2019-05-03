# CST2335-FINAL-PROJECT
# Overview

This program is part of the Final Project developed for Mobile Graphical Development course.
I am enterily responsible for the following code. However, there was another three programs in this Project.
Features that might allow users to navigate between the other programs in this Project were removed.

# Purpose

The Project is assigned to give you experience in: 
- Developing software in a group environment.
- Dividing workload to meet deadlines.
- Designing modular software that allows that division.
- Learning from the work of others.

# Composition

- ListView, ProgressBar, Button, EditText, Toast, Snackbar, Custom Notification Dialog, Toolbar, Fragment, Languages: en-CA, pt-BR
SQL3Lite, SharedPreferences, AsyncTask to retrieve data from an http server, Javadocs.


# Projects

News Feed, Flight Status tracker, Merriam Webster Dictionary, and New York Times Article Search.

# Project Requirements

-	Create an application that reads news stories from the New York Times website.
-	The user should be able to enter a search term. Your application will call the web server to retrieve a list of articles that match the term. Your application should create a list of titles that are retrieved in the results. Clicking on a title should load the rest of the news article, and a link to the article. Clicking on the link should go to a web page Intent with the link text sent in the Intent.
-	An example of a search is: https://api.nytimes.com/svc/search/v2/articlesearch.json?q=Tesla&api-key=89kmL9QdZSaSnHNrZtgRuPmf11e3mPQh. 
-	To search for something other than Tesla, replace that part of the query, and change the API key to what you were given. Don’t forget to use URLEncoder.encode( string, “UTF-8”) to encode your search word.
-	There should be a button on the article to save this article to your device for later viewing. From the list of saved articles, you should be able to delete an item from your list.
-	Your application should save the last topic that was searched to display the next time the application is launched.
