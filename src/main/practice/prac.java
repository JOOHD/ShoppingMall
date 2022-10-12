public class Sample {
    void varTest(int a) {
        a++;
    }

    public static void main(String[] args) {
        int a = 1;
        Sample sample = new Sample();
        sample.varTest(a);
        System.out.println(a);
    }
}
// 결과 1

///////////////////////////////////////////////
public class Sample {
    int varTest(int a) {
        a++;
        return a;
    }

    public static void main(String[] args) {
        int a = 1;
        Sample sample = new Sample();
        a = sample.varTest(a);
        System.out.println(a);
    }
}
// 결과 2

///////////////////////////////////////////////
// 객체 넘기기
public class Sample {

    int a;  // 객체변수 a

    void varTest(Sample sample) {
        sample.a++;
    }

    public static void main(String[] args) {
        Sample sample = new Sample();
        sample.a = 1;
        sample.varTest(sample);
        System.out.println(sample.a);
    }
}
// 결과 2

class Updater {
    void update(int count) {
        count++;
    }
}
// 이댈 실행하면 결과 값이 0, 메소드에 (int 자료형)을 젅달 받았기 때문이다.

class Updater {
    void update(Counter counter) {
        counter.count++;
    }
}
// 이댈 실행해야 결과 값이 1, Counter counter와 같이 객체를 전달받도록 했기 때문이다.

class Counter {
    int count = 0; // 객체변수
}

public class Sample {
    public static void main(String[] args) {

        Counter myCounter = new Counter();
        System.out.println("before update:" + mycounter.count);
        
        Updater myUpdater = new Updater();
        myUpdater.update(myCounter.count);
        System.out.printlnl("After update" + myCounter.count);

        myUpdater.update(myCounter);
        System.out.printlnl("After update" + myCounter);
        /*
          이렇게 메소드의 입력으로 객체를 전달받는 경우에는 메소드가 입력받은 객체를 그대로
          사용하기 때문에 메소드가 객체의 속성값을 변경하면 메소드 수행 후에도 객체의 변경된 속성값 유지
         */
    }
}

class Updater {
    void update(int count) { // int count 객체변수
        count++;    // return 자료형 값이, int가 아닌 count여서 값이 증가가 안됨
    }
}

class Updater {
    void update(Counter counter) {
        counter.count++;
    }
}

myUpdater.update(myCounter.count);
myUpdater.update(myCounter);

/*
    이렇게 메소드의 입력으로 객체를 전달받는 경우에는 메소드가 입력받은 객체를 그대로
    사용하기 때문에 메소드가 객체의 속성값을 변경하면 메소드 수행 후에도 객체의 변경된 속성값 유지
*/

//////////////////////////////////// 상속 /////////////////////////////////////

class Animal {
    String name;

    void setName(String name) {
        this.name = name;
    }
}

class Dog extends Animal {

}

public class Sample {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.setName("poppy");
        System.out.println(dog.name); // poppy 출력
    }
}

//////////////////////////////////// 메소드 오버라이딩 /////////////////////////////////////

class Animal {
    Striing name;

    void setName(String name) {
        this.name = name;
    }
}

class Dog extends Animal {
    void sleep() {
        System.out.println(this.name+" zzz");
    }
}

class HouseDog extends Dog {
    void sleep() {
        System.out.println(this.name + " zzz in house");
    }

    void sleep(int hour) {
        System.out.println(this.name+ " zzz in house for " + hour + " hours");
    } // 입력항목이 다른 경우 동일한 이름의 메소드를 만들 수 있는데 이를 메소드 오버로딩이라고 부른다.
}

public class Sample {
    public static void main(String[] args) {
        HouseDog houseDog = new HouseDog();
        houseDog.setName("happpy");
        houseDog.sleep();  // happy zzz 출력
        houseDog.sleep(3); // happy zzz in house for 3 hours 출력
    }
}
// 변경은 오버라이딩, 추가는 오버로딩이다.

//////////////////////////////////// 생성자 /////////////////////////////////////

class Animal {
    Striing name;

    void setName(String name) {
        this.name = name;
    }
}

class Dog extends Animal {
    void sleep() {
        System.out.println(this.name+" zzz");
    }
}

class HouseDog extends Dog {

    // 메소드 명이 클래스명과 동일하고 리턴 자료형을 정의하지 않는 메소드를 생성자라고 한다.
    /*
        생성자 규칙
        1.클래스명과 메소드명이 동일하다.
        2.리턴타입을 정의하지 않는다. (void도 사용하지 않는다.) 
        3.생성자는 객체가 생성될 때 호출된다. 즉, 생성자ㅏ는 다음과 같이 new 키워드가 사용될 때 호출
    */ 
    HouseDog(String name) { // HouseDog 클래스에 만든 생성자는 다음과 같이 '입력값'으로 문자열을 필요로 하는 생성자이다.
                            //    ex) HouseDog dog = new HouseDog("happy"); 생성자 호출시 문자열을 전달해야 한다.
        this.setName(name);
    }

    void sleep() {
        System.out.println(this.name + " zzz in house");
    }

    void sleep(int hour) {
        System.out.println(this.name+ " zzz in house for " + hour + " hours");
    } // 입력항목이 다른 경우 동일한 이름의 메소드를 만들 수 있는데 이를 메소드 오버로딩이라고 부른다.
}

public class Sample {
    public static void main(String[] args) {
        HouseDog houseDog = new HouseDog();
        System.out.println(dog.name); // null 출력

        HouseDog dog = new HouseDog("happy");
        System.out.println(dog.name);
    }
}

//////////////////////////////////// 생성자 오버로딩 /////////////////////////////////////

class Animal {
    String name;

    void setName(String name) {
        this.name = name;
    }
}

class Dog extends Animal {
    void sleep() {
        System.out.println(this.name + " zzz");
    }
}

class HouseDog extends Dog {
    HouseDog(String name) {
        this.setName(name);
    }

    HouseDog(int type) {
        if (type == 1) {
            this.setName("yorkshire");
        } else if (type == 2) {
            this.setName("bulldog");
        }
    }

    void sleep() {
        System.out.println(this.name + " zzz in house");
    }

    void sleep(int hour) {
        System.out.println(this.name + " zzz in house for " + hour + " hours");
    }
}

public class Sample {
    public static void main(String[] args) {
        HouseDog happy = new HouseDog("happy");
        HouseDog yorkshire = new HouseDog(1);
        System.out.println(happy.name);  // happy 출력
        System.out.println(yorkshire.name);  // yorkshire 출력
    }
}

//////////////////////////////////// 인터페이스 /////////////////////////////////////

class Animal {
    String name;

    void setName(String name) {
        this.name = name;
    }
}

class Tiger extends Animal {
}

class Lion extends Animal {
}

class ZooKeeper {
    void feed(Tiger tiger) { // 호랑이가 오면 사과를 던져 준다.
        System.out.println("feed apple");
    }

    void feed(Lion lion) {
        System.out.println("fedd banana");
    }

    public void feed(Crocodile crocodile) {
        System.out.println("feed strawberry");
    }

    public void feed(Leopard leopard) {
        System.out.println("feed orange");
    }
}

public class Sample {
    public static void main(String[] args) {
        ZooKeeper zooKeeper = new ZooKeeper();
        Tiger tiger = new Tiger();
        Lion lion = new Lion();
        zooKeeper.feed(tiger); // feed apple 출력
        zooKeeper.feed(lion);  // feed banana 출력
    }
}
////////////////////////////////////////////////////////////////////////////////

interface Predator {

    /*
      인터페이스는 클래스와 마찬가지로 Predator.java 와 같은 단독 파일로 저장하는 것이
      일반적인 방법이다. 여기서는 설명의 편의를 위해 Sample.java파일의 최상단에 작성
     */
}

class Animal {
    String name;

    void setName(String name) {
        this.name = name;
    }
}

// 이와같이 객체가 한 개 이상의 자료형 타입을 갖게되는 특성을 다형성이라고 한다.
class Tiger extends Animal implements Predator {
    public String getFood() {
        return "apple";
    }
}

class Lion extends Animal implements Predator {
    public String getFood() {
        return "banana";
    }
}

class ZooKeeper {
    void feed(Predator predator) {
        System.out.println("feed" +predator.getFood());
    }
}

public class Sample {
    public static void main(String[] args) {
        ZooKeeper zooKeeper = new ZooKeeper();
        Tiger tiger = new Tiger();
        Lion lion = new Lion();
        zooKeeper.feed(tiger); // feed apple 출력
        zooKeeper.feed(lion);  // feed banana 출력
    }
}
/////////////////////////////////////////////////////////////////////////////

interface Predator {
    (... 생략 ...)
}

interface Barkable {
    void bark();
}

// BarkablePredator는 Predator의 getFood 메소드, Barkable의 bark 메소드를 그대로 사용 가능
interface BarkablePredator extends Predator, Barkable {
}

class Animal {
    (... 생략 ...)
}

class Tiger extends Animal implements Predator, Barkable {
    public String getFood() {
        return "apple";
    }

    public void bark() {
        System.out.println("어흥");
    }
}

class Lion extends Animal implements Predator, Barkable {
    public String getFood() {
        return "banana";
    }

    public void bark() {
        System.out.println("으르렁");
    }
}

class ZooKeeper {
    (... 생략 ...)
}

class Bouncer {
/*
  바뀌기 전
  void barkAnimal(Animal animal) {
    if (animal instanceof Tiger) {
        System.out.println("어흥");
    } else if (animal instanceof Lion) {
        System.out.println("으르렁");
    }
} 
 */

// 바꾼 후
    void barkAnimal(Barkable animal) {  // Animal 대신 Barkable을 사용
        animal.bark();
    }
}

public class Sample {
    (... 생략 ...)
}

Tiger tiger = new Tiger();  // Tiger is a Tiger
Animal animal = new Tiger();  // Tiger is a Animal
Predator predator = new Tiger();  // Tiger is a Predator
Barkable barkable = new Tiger();  // Tiger is a Barkable

// Tiger 클래스의 객체는 다음과 같이 여러가지 자료형으로 표현할 수 있다.

// 전체 코드
interface Predator {
    String getFood(); // getFood 변수 선언

    default void printFood() {
        System.out.printf("my food is %s\n", getFood());
    }

    int LEG_COUNT = 4;  // 인터페이스 상수

    static int speed() {
        return LEG_COUNT * 30;
    }
}

interface Barkable {
    void bark();
}

interface BarkablePredator extends Predator, Barkable {
}

class Animal {
    String name;

    void setName(String name) {
        this.name = name;
    }
}

class Tiger extends Animal implements Predator, Barkable {
    public String getFood() {
        return "apple";
    }

    public void bark() {
        System.out.println("어흥");
    }
}

class Lion extends Animal implements BarkablePredator {
    public String getFood() {
        return "banana";
    }

    public void bark() {
        System.out.println("으르렁");
    }
}

class ZooKeeper {
    void feed(Predator predator) {
        System.out.println("feed " + predator.getFood());
    }
}

class Bouncer {
    void barkAnimal(Barkable animal) {
        animal.bark();
    }
}

public class Sample {
    public static void main(String[] args) {
        Tiger tiger = new Tiger();
        Lion lion = new Lion();

        Bouncer bouncer = new Bouncer();
        bouncer.barkAnimal(tiger);
        bouncer.barkAnimal(lion);
    }
}
///////////////////////////////// 연습문제 ///////////////////////////////

class Calculator {
    int value;

    // 기본 생성자
    Calculator() { 
        this.value = 0;
    }

    void add(int val) {
        this.value += val;
    }

    int getValue() {
        return this.value;
    }
}
// 클래스에 생성된 메소드들은 main 클래스에서 생성된 객체에 시용된다.

public class Sample {
    public static void main(String[] args) {
        Calculator cal = new Calculator();
        cal.add(10);
        System.out.printlln(cal.getValue()); // 10 출력
    }
}

UpgradeCalculator cal = new UpgradeCalculator();
cal.add(10);
cal.minus(3);
System.out.println(cal.getValue());  // 10에서 7을 뺀 3을 출력


///////////////////////////////// 연습문제2 ///////////////////////////////

class Calculator {
    int value;

    Calculator() {
        this.value = 0;
    }

    void add(int val) {
        this.value += val;
    }

    int getValue() {
        return this.value;
    }
}

class MaxLimitCalculator extends Calculator {
    void add(int val) {
        this.value += val;
        if (this.value > 100) {
            this.value = 100;
        }
    }
}

public class Sample {
    public static void main(String[] args) {
        MaxLimitCalculator cal = new MaxLimitCalculator();
        cal.add(50);  // 50 더하기
        cal.add(60);  // 60 더하기
        System.out.println(cal.getValue());  // 100 출력
    }
}

