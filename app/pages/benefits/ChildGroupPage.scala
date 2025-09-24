
package pages.benefits

import models.Child
import play.api.libs.json.JsPath
import queries.{Gettable, Settable}


case object ChildGroup
  extends Gettable[List[Child]]
    with Settable[List[Child]] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "children"

}


case class ChildWithIndex(index: Int)
  extends Settable[Array[Child]] {

  override def path: JsPath = JsPath \ toString \ index

  override def toString: String = "children"
}

