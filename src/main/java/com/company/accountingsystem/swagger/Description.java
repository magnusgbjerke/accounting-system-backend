package com.company.accountingsystem.swagger;

public interface Description {
    String DESCRIPTION_TEXT = """
            API documentation for the Accounting System application
            
            ## Usage
            
            - **Download the spec** [api-docs](http://localhost:8080/v3/api-docs) file, it is a OpenAPI Specification.
            
            
            ## Overview
            
            - **Date** follows the [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) standard. Format: YYYY-MM-DD.
            
            - **DateTime** follows the [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) standard. Format YYYY-MM-DDThh:mm:ss.
            
            ## Response envelope
            
            ### Single value
            
            ```json
            {
             object 1
            }
            ```
            
            ### Multiple values
            
            ```json
            [
             {object 1},
             {object 2},
             ...
            ]
            ```

            ## Error/warning envelope
            
            
            ```json
            {
             "status": ###,             // {number} HTTP status code
             "message": "###",          // {string} Feedback message,
             "path": "###",             // {string} Path to where the error/warning occurred
             "timestamp": "###",        // {timestamp} The time the error/warning occurred
             "link": "###",             // {string} Link to our API docs
            }
            ```
            
            ## Status codes / Error codes
            
            * **200 - OK**
            * **201 - Created**
            * **204 - No Content**
            * **400 - Bad Request**
            * **404 - Not Found**
            * **409 - Conflict**
            * **500 - Internal Error**
            """;
}




