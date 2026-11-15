package fix

import scala.meta._
import scalafix.v1._

/** House style: a space before every argument, parameter and type-parameter list.
  *
  * `def f[T](x: Int)(y: Int)` becomes `def f [T] (x: Int) (y: Int)`; `List[A]`, `Some(x)`, `@nowarn("...")` and
  * `f()` become `List [A]`, `Some (x)`, `@nowarn ("...")` and `f ()`. A space is added only where `(` or `[` touches
  * the end of a name, `this`, `super`, `)` or `]`; nothing else changes. Symbolic names are left alone, so a prefix
  * operator stays `-(x)` and `~(x)`. Comments and strings are tokens of their own, so they are never touched.
  */
class SpaceBeforeLists extends SyntacticRule ("SpaceBeforeLists") {

  private def opens (t: Token): Boolean = t match {
    case _: Token.LeftParen | _: Token.LeftBracket => true
    case _ => false
  }

  private def ends (t: Token): Boolean = t match {
    case id: Token.Ident => isAlphanumeric (id.value)
    case _: Token.RightParen | _: Token.RightBracket | _: Token.KwThis | _: Token.KwSuper => true
    case _ => false
  }

  private def isAlphanumeric (name: String): Boolean =
    name.startsWith ("`") || name.headOption.exists (c => c.isLetter || c == '_' || c == '$')

  override def fix (implicit doc: SyntacticDocument): Patch = {
    val tokens = doc.tokens
    (1 until tokens.length).iterator
      .filter (i => opens (tokens (i)) && ends (tokens (i - 1)))
      .map (i => Patch.addLeft (tokens (i), " "))
      .foldLeft (Patch.empty) (_ + _)
  }
}
