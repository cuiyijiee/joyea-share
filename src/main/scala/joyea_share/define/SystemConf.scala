package joyea_share.define


object SystemConf {

    def constants: Constants = {

        val constantsConfig = xitrum.Config.application.getConfig("constants")
        Constants(
            baseSavePath = constantsConfig.getString("base_save_path"),
            defaultTmpUploadPath = constantsConfig.getString("default_tmp_upload_path"),
            defaultPathType = constantsConfig.getString("base_path_type"),
        )
    }

}

case class Constants(
                      baseSavePath: String,
                      defaultTmpUploadPath: String,
                      defaultPathType: String
                    )