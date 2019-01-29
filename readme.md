* SharingFiles
** 概要
N予備校6章 【実習】ファイル共有 の実習用プロジェクト。
コンテンツプロバイダの一一種であるFileProvideを使って、ファイルを共有する

FileSelectiActivity:ファイルを選択するコンテンツプロバイダとして働くアクティビティ

MainActivity:ファイル選択の結果を表示するアクティビティ
ACTION_PICKを含んだインテントを投げ、startActivityForResult()で結果を処理。
処理内容としては、
1.返されたイメージURIをImageViewに設定
2.ファイルの各種データを表示


TODO
1.FileDescriptorの役割確認(このサンプルでは使われていない)
2.Android Developersの記載のように複数のデータから選択するようにFileSelectActivityを修正(復習をかねて、１からつくってみる)
