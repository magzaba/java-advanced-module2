# java-advanced-module2
Practical task for module 2 of Java Advanced: Backend Core Program


### Task 1 - Das Experiment




### Task 2 - Deadlocks



### Task 3 - Where's your bus dude?

#### prerequisite:

-  Message class that represents a message with a topic and payload.

#### Message bus implemented using Producer-Consumer pattern.

1. MessageBus class that manages message queues for different topics and provides synchronized methods to post and consume messages.
2. Producer randomly generates messages with random payloads and posts them to the message bus on their respective topics.
3. Consumer listens on a specific topic and consumes messages as they become available, logging the message payload to the console.
4. Producer and Consumer classes implement the Runnable interface to be executed in separate threads.
5. Each message is consumed only once.
6. Application allows many consumers to consume messages in parallel as it is presented in MessageBusApp class,
which sets up the message bus, starts multiple producers and consumers, and assigns them to different topics.


### Task 4 - Blocking object pool



### Task 5 - Currency Exchange application

