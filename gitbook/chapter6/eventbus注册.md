# register做了哪些

注册的对你在EventBus里充当一个订阅者,通过它拿到Class对象，调用findSubscriberMethods找到这个类申明的回调方法
```java
public void register(Object subscriber) {
    Class<?> subscriberClass = subscriber.getClass();
    List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
    synchronized (this) {
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            subscribe(subscriber, subscriberMethod);
        }
    }
}

```

系统为每个类提供一个List<SubscriberMethod>缓存，不用每次通过反射去获取回调方法

```java
private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new ConcurrentHashMap<>();

```

找到订阅类的回调方法
```java
   List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        //先从缓存里拿，如果拿到了，就直接返回
        if (subscriberMethods != null) {
            return subscriberMethods;
        }

        if (ignoreGeneratedIndex) {
        //通过反射找到回调函数
            subscriberMethods = findUsingReflection(subscriberClass);
        } else {
            subscriberMethods = findUsingInfo(subscriberClass);
        }
        if (subscriberMethods.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass
                    + " and its super classes have no public methods with the @Subscribe annotation");
        } else {
            METHOD_CACHE.put(subscriberClass, subscriberMethods);
            return subscriberMethods;
        }
    }

```

先了解几个集合
```java
//key:Event  value:List<subscriber+subscriberMethod>
Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType

//key:subscriber value:List<Event>
private final Map<Object, List<Class<?>>> typesBySubscriber;

//key:EventType value Event
private final Map<Class<?>, Object> stickyEvents;

```

遍历每个方法，去订阅
```java
 for (SubscriberMethod subscriberMethod : subscriberMethods) {
            subscribe(subscriber, subscriberMethod);
        }

```



Subscription(subscriber,subscriberMethod)装有订阅者和订阅者的回调，eventType则是事件。
subscribe把事件事件接收者和事件接收者函数联系起来。


每个被@Subscribe声明的方法都会被加到subscriptionsByEventType当中，也就是说通过subscriptionsByEventType可以获取任意EventType的所有回调

```java
 private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        //method-subscriber
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            //放入到缓存当中
            subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            //如果已经包含该订阅者，就说明注册过了
            if (subscriptions.contains(newSubscription)) {
                throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event "
                        + eventType);
            }
        }
        //...ignore code

```

然后是把新的订阅者插入到List当中，考虑优先级
```java
 int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size || subscriberMethod.priority > subscriptions.get(i).subscriberMethod.priority) {
                subscriptions.add(i, newSubscription);
                break;
            }
        }


```

缓存订阅者所关心的事件
```java
     List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);

```

如果回调申明是粘性的，系统里有一个粘性事件，就直接回调
```java
  if (subscriberMethod.sticky) {
            if (eventInheritance) {
                // Existing sticky events of all subclasses of eventType have to be considered.
                // Note: Iterating over all events may be inefficient with lots of sticky events,
                // thus data structure should be changed to allow a more efficient lookup
                // (e.g. an additional map storing sub classes of super classes: Class -> List<Class>).
                Set<Map.Entry<Class<?>, Object>> entries = stickyEvents.entrySet();
                for (Map.Entry<Class<?>, Object> entry : entries) {
                    Class<?> candidateEventType = entry.getKey();
                    if (eventType.isAssignableFrom(candidateEventType)) {
                        Object stickyEvent = entry.getValue();
                        checkPostStickyEventToSubscription(newSubscription, stickyEvent);
                    }
                }
            } else {
                Object stickyEvent = stickyEvents.get(eventType);
                checkPostStickyEventToSubscription(newSubscription, stickyEvent);
            }
        }

```