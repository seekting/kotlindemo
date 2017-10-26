# EventBus

## EventBus对象的创建
方式一：单例获取
EventBus对象通过单例的设计模式保证在进程中唯一

```java
   public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {//此处通过双保险去确保在进程中只有一个对象
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

```
方式二：通过Builder.installDefault获取
```java
    public EventBus installDefaultEventBus() {
        synchronized (EventBus.class) {
            if (EventBus.defaultInstance != null) {
                throw new EventBusException("Default instance already exists." +
                        " It may be only set once before it's used the first time to ensure consistent behavior.");
            }
            EventBus.defaultInstance = build();
            return EventBus.defaultInstance;
        }
    }

```
方式三：通过Builder自定义生成(这种写法将需要你自己去维护一个单实例的EventBus)
```java
 public EventBus build() {
        return new EventBus(this);
    }


```

