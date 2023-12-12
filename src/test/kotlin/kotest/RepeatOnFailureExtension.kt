//Описание/Пошаговая инструкция выполнения домашнего задания:
//Реализуйте расширение для Kotest, которое позволить повторить запуск тестов, в которых произошла ошибка.
//
//Создайте класс RepeatOnFailureExtension, который импементирует TestCaseExtension
//Создайте внутри класса переменную максимального количества перезапусков. Значение - любое на ваш выбор.
//Переопределите метод intercept так, чтобы если результат выполнения теста - ошибка или неудача -
// тест перепрогонялся заново заданное в пункте 2 количество раз.
//Примените данное расширение ко всем тестам в проекте.
//Напишите минимум 1 позитивный и 1 негативный тест с использованием данного расширения
//Вам понадобится 120-180 минут на выполнение домашнего задания.
//Если возникнут сложности, вы всегда можете обсудить их с одногруппниками или задать вопрос преподавателю Slack

package kotest

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe


// Создал как object , т.к AbstractProjectConfig extensions ожидает object , а не класс
object RepeatOnFailureExtension : TestCaseExtension {
    private val retryCount: Int = 3
    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        var result = execute(testCase)
        var retries = 0
        while (result.isErrorOrFailure && retries < retryCount) {
            println("Test ${testCase.name} failed. Retrying...")
            result = execute(testCase)
            retries++
        }
        return result
    }
}

class MyFirstTestClass : FunSpec({
    test("positive") {
        1 + 2 shouldBe 3
    }
    test("negative") {
        1 + 2 shouldBe 4
    }

})

class CustomConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(RepeatOnFailureExtension)
}

