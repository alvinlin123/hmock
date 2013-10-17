HMock
========

<b>Problem:</b>

Imagine you are writing a client application that interact with some third party REST API. You wrote some code and you would 
like to unit test it. Yes, you can test it by interact wit the REST API directly, but then your tests will be 
dependent on the ever changing returned data which you have no control of. Boom! your test cases become unstable. 
Furthermore, how can you test edge cases if the REST API doesn't contain data to trigger edge cases? 


<b>Solution:</b>

Mock the REST API locally! Hence HMock. The concept of HMock is to start a HTTP server locally, and ther server can be
configured (through some DSL) to returne pre-defined data base on the request. 

For example, you can configure in such a way that:
* Upon receiving /employee/John return a good employee data saved in a file
* Upon receiving /employee/Jack return a invalid employee data saved in a file

So that if you point your client application to the locally mocked server, you can test the client's behaviour with 
stable data; you will also be able to handcraft data to trigger may error/edge cases in your application.

This project is still very young, and alot of design can change, so detailed documentation is not included for now.

Everyone is welcome to contribute!

