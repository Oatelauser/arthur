package com.arthur.common.invoke;

import org.junit.jupiter.api.Test;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodType;
import java.util.function.ToIntFunction;

public class LambdaMetafactorysTest {

	static class GrandFather {
		void thinking() {
			System.out.println("I am grandFather!");
		}
	}
	static class Father extends GrandFather {
		void thinking() {
			System.out.println("I am father!");
		}
	}

	@Test
	public void invokeSuper() throws Throwable {
		//Father father = new Father();
		//CallSite callSite = LambdaMetafactorys.createLambda(Consumer.class,
		//	findSuperMethod(Father.class, GrandFather.class, "thinking", methodType(void.class)));
		CallSite callSite = LambdaMetafactorys.createLambda(ToIntFunction.class,
			MethodHandlers.findMethod(User.class, "getAge", MethodType.methodType(int.class)));
		ToIntFunction<User> invokeExact = (ToIntFunction<User>) callSite.getTarget().invokeExact();
		invokeExact.applyAsInt(new User());
	}

	public static void main(String[] args) throws Throwable {
		CallSite callSite = LambdaMetafactorys.createLambda(ToIntFunction.class,
			MethodHandlers.findMethod(User.class, "getAge", MethodType.methodType(int.class)));
		ToIntFunction<User> invokeExact = (ToIntFunction<User>) callSite.getTarget().invokeExact();
		invokeExact.applyAsInt(new User());
	}

}
