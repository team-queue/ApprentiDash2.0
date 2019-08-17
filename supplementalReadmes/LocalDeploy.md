# Deploying Locally

If you want to fork our code and use it for your own project, you will need to take a few steps after cloning the repo. 

1) Create a local psql Database named "apprenti"

      (No need to set up tables, our app will take care of that.)
    
      If you need help installing Postgres, you can use these resources: 
    
      Windows users, if using the Ubuntu app, can use [these instructions.](https://github.com/codefellows/code-301-prework/blob/master/windows/wsl.md)
    
      Mac users, [use these](https://github.com/codefellows/code-301-prework/blob/master/setup_local_env.md) (Scroll to the bottom for postgres).
    
2) Set environment variables for the application properties.
    
    For example, to run locally you can set the DATABASE_URL to
        
        jdbc:postgresql://localhost:5432/apprenti
         
      Username and Password may also be required if you are running on a Windows PC.
      
      For example: 

        spring.datasource.url=${DATABASE_URL}
        spring.datasource.username=${DATABASE_USERNAME}
        spring.datasource.password=${DATABASE_PASSWORD}
      
   We recommend using [environment variables](https://medium.com/chingu/an-introduction-to-environment-variables-and-how-to-use-them-f602f66d15fa) if you are using a password. 
   
   Set your username and password according to your psql settings.
   
3) Please make sure the user running locally has the SNSFullAcess permission. So you can receive the message.

    If you don't want to set up your SNS service right away, you can have your test users opt out of notifications.
    
4) Build and run

5) Open your browser to localhost:5000