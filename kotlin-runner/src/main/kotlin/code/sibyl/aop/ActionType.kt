package code.sibyl.aop

enum class ActionType {

    INSERT,
    UPDATE,
    OTHER,
    ;

}

fun main(){
    println(ActionType.UPDATE)
}