import java.lang.reflect.*;
import jdk.example.Person;
public final class $Proxy0 extends MrliuProxy
	implements jdk.sourceCode.Person{
public $Proxy0(MrliuInvocationHandler invocationhandler){
super(invocationhandler);
}
public final void findLove() throws Throwable{
Method m = Class.forName("jdk.sourceCode.Person").getMethod("findLove", new Class[0]);
super.h.invoke(this, m, null);
return;
}
}