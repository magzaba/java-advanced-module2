# java-advanced-module2
Practical task for module 2 of Java Advanced: Backend Core Program


### Task 1 - Das Experiment

#### Running the Experiment

The <code>Experiment</code> class is designed to demonstrate the behavior of different <code>Map</code> implementations under concurrent modifications. 
It uses two threads: one for adding elements to the map and another for periodically summing the values in the map. 
The class is structured to allow easy switching between different types of maps, such as <code>HashMap</code>, <code>ConcurrentHashMap</code>, and a synchronized version of <code>HashMap</code> using <code>Collections.synchronizedMap()</code>. 
This setup helps illustrate the thread-safety characteristics of these map implementations.

#### Custom ThreadSafeMap implementation

1. SynchronizedThreadSafeMap

- Data Structure: The map entries are stored in a List of Entry objects, where Entry is a static inner class that holds key-value pairs.
- Thread safety: All methods that modify or access the list are synchronized to maintain thread safety.
- Limitations: This implementation is not optimized for performance in scenarios with a large number of entries or high concurrency.

2. LockFreeThreadSafeMap

- Data Structure: The map entries are stored in a <code>ConcurrentLinkedQueue</code>, which is a thread-safe, non-blocking queue provided by Java's concurrent package. 
- Thread safety: This implementation leverages the thread-safe properties of <code>ConcurrentLinkedQueue</code> to manage concurrent access to the entries.
- Limitations: This implementation may not be as performant as using a dedicated concurrent map like <code>ConcurrentHashMap</code>
  due to the linear time complexity of operations like put and get, which involve streaming through the queue,
  also the <code>size()</code> method, while simple, involves a full traversal of the queue and thus can be expensive in terms of performance.

### Task 2 - Deadlocks

- The <code>CollectionAnalyzer</code> class is designed to perform concurrent operations on a shared <code>ArrayList<Integer></code> collection 
using multiple threads. Each thread performs a specific task: adding random numbers to the collection, 
calculating the sum of the numbers, and calculating the square root of the sum of squares of the numbers.

- Thread Safety: The use of synchronized on the shared collection ensures that only one thread can modify or read the collection at a time, 
preventing data corruption and ensuring consistency.

- Preventing from the deadlock: The <code>CollectionAnalyzer</code> class avoids deadlocks by using a single shared resource (collection) for synchronization, 
ensuring that all threads lock and unlock this single object in a consistent and straightforward manner. 
Each thread acquires the lock, performs its operation, and releases the lock promptly before sleeping, which minimizes lock contention and eliminates the conditions necessary for a deadlock.


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

