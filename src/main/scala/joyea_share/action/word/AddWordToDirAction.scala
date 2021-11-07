package joyea_share.action.word

import joyea_share.action.BaseAction
import joyea_share.model.DirWord
import xitrum.annotation.POST

@POST("api/v1/word/add")
class AddWordToDirAction extends BaseAction[AddWordToDirReq] {
  override def safeExecute(req: AddWordToDirReq): Unit = {
    DirWord.deleteDirWord(req.neid).onComplete(safeResponse[Int](_, info => {
      DirWord.create(req.word.map(info => {
        DirWord(
          id = 0,
          neid = req.neid,
          wordId = info.id,
          wordName = info.title
        )
      })).onComplete(safeResponse[Int](_, info => {
        baseResponseSuccess(info)
      }))
    }))
  }
}

case class AddWordToDirReq(neid: String, word: Seq[WordInfo])

case class WordInfo(
                     id: String,
                     title: String
                   )