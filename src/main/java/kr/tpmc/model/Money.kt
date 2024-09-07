package kr.tpmc.model

import io.github.asdqwenm123.SimplePlayer
import net.kyori.adventure.text.Component
import java.time.LocalDateTime

class Money(
    @Transient
    var guild: Guild
) {
    companion object {
        const val MAINTENANCE_COST: Long = /*10L*/1000000L
        const val INTEREST_RATE: Double = 0.1
        const val MAXIMUM_LIMIT: Long = /*1000000L*/3000000L
        const val ESTABLISHMENT_FUND: Long = 10000000L
        const val RENAMING_COST: Long = 100000000L
    }

    private var nextWithdrawalDate: LocalDateTime = LocalDateTime.now()./*plusSeconds(1)*/plusWeeks(1)
    private var nextInterestDate: LocalDateTime = LocalDateTime.now()./*plusSeconds(1)*/plusDays(1)
    private var loan: Long = 0L
    private var money: Long = 0L

    fun makeScheduledWithdrawal() {
        if (nextWithdrawalDate.isBefore(LocalDateTime.now())) {
            val amount = guild.memberList.members.count().toLong() * MAINTENANCE_COST
            println(amount)
            val result = withdraw(amount)

            if (result.isError) {
                if (result.value == MoneyStatus.MINUS_MONEY) {
                    depositLoan(amount)
                    nextWithdrawalDate = nextWithdrawalDate./*plusSeconds(1)*/plusWeeks(1)
                } else {
                    nextWithdrawalDate = nextWithdrawalDate./*plusSeconds(1)*/plusWeeks(1)
                }
            } else {
                withdraw(amount)
                nextWithdrawalDate = nextWithdrawalDate./*plusSeconds(1)*/plusWeeks(1)
            }

        }
//        println("$money, $loan")

        if (nextInterestDate.isBefore(LocalDateTime.now())) {
            val result = depositLoan((loan * INTEREST_RATE).toLong())
            if (!result.isError) {
                nextInterestDate = nextInterestDate./*plusSeconds(1)*/plusDays(1)
            }
        }
//        println("$money, $loan")
//        SimplePlayer.getPlayer(guild.owner).sendMessage(Component.text("$money, $loan"))
    }

    fun getLoan(): Long = loan
    fun getMoney(): Long = money
    fun getNextInterestDate(): LocalDateTime = nextInterestDate

    fun withdrawLoan(amount: Long): Result<MoneyStatus> {
        if (amount < 0) {
            return Result.error(MoneyStatus.MINUS_AMOUNT)
        }

        val r: Long = loan - amount

        if (((loan xor amount) and (loan xor r)) < 0L) {
            return Result.error(MoneyStatus.OVERFLOW)
        }

        if (r < 0) {
            return Result.error(MoneyStatus.MINUS_MONEY)
        }

        loan = r
        return Result.success(MoneyStatus.SUCCESS)
    }

    fun depositLoan(amount: Long): Result<MoneyStatus> {
        if (amount < 0) {
            return Result.error(MoneyStatus.MINUS_AMOUNT)
        }

        val r: Long = loan + amount

        if (((loan xor r) and (amount xor r)) < 0L) {
            return Result.error(MoneyStatus.OVERFLOW)
        }

        loan += amount
        return Result.success(MoneyStatus.SUCCESS)
    }

    fun withdraw(amount: Long): Result<MoneyStatus> {
        if (amount < 0) {
            return Result.error(MoneyStatus.MINUS_AMOUNT)
        }

        val r: Long = money - amount

        if (((money xor amount) and (money xor r)) < 0L) {
            return Result.error(MoneyStatus.OVERFLOW)
        }

        if (r < 0) {
            return Result.error(MoneyStatus.MINUS_MONEY)
        }

        money = r
        return Result.success(MoneyStatus.SUCCESS)
    }

    fun deposit(amount: Long): Result<MoneyStatus> {
        if (amount < 0) {
            return Result.error(MoneyStatus.MINUS_AMOUNT)
        }

        val r: Long = money + amount

        if (((money xor r) and (amount xor r)) < 0L) {
            return Result.error(MoneyStatus.OVERFLOW)
        }

        money += amount
        return Result.success(MoneyStatus.SUCCESS)
    }
}