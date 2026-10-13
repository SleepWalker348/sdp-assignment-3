### 1. What are the two independent dimensions?
The two independent dimensions in this system are:
1. **Abstraction**: The type/business logic of the notification (`UrgentNotification`, `EncryptedNotification`, `ScheduledBatchNotification`, `MarketingNotification`).
2. **Implementation**: The delivery mechanism/channel through which messages are actually transmitted (`TelegramMessageSender`, `EmailMessageSender`, `SmsMessageSender`, `KafkaEventSender`, `PushNotificationSender`).

### 2. Where exactly is the Bridge?
The Bridge is represented by the aggregate composition relationship field `protected MessageSender sender;` inside the abstract class `Notification`. It maintains a reference to the `MessageSender` interface, allowing calls to be delegated dynamically at runtime.

### 3. Why is inheritance problematic here?
Inheritance forces a tight static coupling, leading to an **$N \times M$ class explosion**. Adding $N$ notification types and $M$ message delivery providers would require creating $N \times M$ separate subclasses (e.g., `UrgentTelegramNotification`, `UrgentEmailNotification`, `EncryptedTelegramNotification`, etc.). With Bridge, it requires only $N + M$ classes.

### 4. What happens when a new provider is added?
To add a new delivery channel (e.g., `PushNotificationSender`), we simply implement the `MessageSender` interface. None of the existing `Notification` classes need to be modified, recompiled, or tested again.

### 5. What happens when a new Notification type is added?
To add a new notification type (e.g., `MarketingNotification`), we simply extend the base `Notification` abstract class. It immediately works with all existing and future `MessageSender` implementations without altering any sender code.

### 6. Why is this Bridge rather than Adapter?
- **Bridge** is an **architectural pattern designed up-front** to let abstractions and implementations evolve independently in two different dimensions.
- **Adapter** is an **structural wrapper applied after the fact** to make two existing, incompatible interfaces work together.