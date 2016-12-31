package com.programmaticallyspeaking.ncd.nashorn.mirrors

import com.programmaticallyspeaking.ncd.host.{LazyNode, SimpleValue, ValueNode}
import com.programmaticallyspeaking.ncd.nashorn.DynamicInvoker
import com.sun.jdi.{ArrayReference, ObjectReference, ThreadReference}

/**
  * Mirror for `jdk.nashorn.internal.runtime.ScriptObject`. This class doesn't do marshalling - the return value of
  * methods is typically a JDI [[com.sun.jdi.Value]].
  *
  * @param thread thread on which method calls will take place
  * @param scriptObject the `ScriptObject` instance to interact with
  */
class ScriptObjectMirror(thread: ThreadReference, val scriptObject: ObjectReference) {
  import ScriptObjectMirror._
  lazy val invoker = new DynamicInvoker(thread, scriptObject)

  def getClassName = invoker.getClassName()

  def isArray = invoker.isArray()

  def entrySet() = invoker.entrySet().asInstanceOf[ObjectReference]

  def put(key: AnyRef, value: AnyRef, isStrict: Boolean) =
    invoker.applyDynamic(putObjectObjectBoolSignature)(key, value, isStrict)

  def actualToString = invoker.applyDynamic("toString")()

  override def toString = "ScriptObjectMirror (maybe you meant actualToString?)"
}

object ScriptObjectMirror {
  val putObjectObjectBoolSignature = "put(Ljava/lang/Object;Ljava/lang/Object;Z)Ljava/lang/Object;"
}