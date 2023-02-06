package md.edi.mobilewaiter.utils.enums

enum class EnumLicenseErrors(val code: Int) {
    InternalError(-1),
    CompanyNotFound(1),
    PlatformNotExist(3),
    LicenseNotExist(124),
    CodeNotExist(187);


    companion object {
        fun getByValue(value: Int) = values().first { it.code == value }
    }
}