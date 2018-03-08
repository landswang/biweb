-- 新增问题类型
INSERT INTO `article_type` VALUES (95, '注册登录');
INSERT INTO `article_type` VALUES (96, '交易委托');
INSERT INTO `article_type` VALUES (97, '充币提币');
INSERT INTO `article_type` VALUES (98, '充值提现');
INSERT INTO `article_type` VALUES (99, '安全设置');
-- 新增常见问题
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (95, '注册流程', '使用您的手机号注册，无邀请码可不填写，输入图形验证码后需点击“输入图形验证获取短信”按钮获取短信验证码。', NULL, '2018-2-3 15:55:38');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (95, '收不到验证码怎么办？', '收不到验证码时，检查手机拦截软件或者手机自带拦截功能是否拦截了短信验证码，如果未设置拦截，获取一次验证码未收到，请隔5分钟后再尝试（同一时间多次获取验证码，系统将会判断为恶意获取短信验证码从而锁定）。如多次未收到短信验证码，请联系客服。', NULL, '2018-2-3 15:55:38');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (95, '安全及认证', '新注册用户需设置交易密码，完成普通实名认证之后才能正常交易，提币需进行高级认证，确保账户资金安全。', NULL, '2018-2-3 15:56:49');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (95, '为什么要进行实名认证？', '为防止洗钱，所有在BItop交易网交易的用户均需要实名认证。另外，实名认证也可以加强账户安全，当账户出现异常时，平台通过认证信息进行取证审核，解除异常。', NULL, '2018-2-3 15:58:02');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (98, '如何充值提现？', '在C2C交易中买入对应充值CNT，卖出对应提现CNT，CNT单价固定。充值只能使用网银实时到账汇款或支付宝2小时到账汇款，切勿使用其他转账方式（如微信、平安付、京东、易支付、支付宝次日到等）。', NULL, '2018-2-3 15:58:44');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (98, '充值未到账如何处理？', '首先请确认网银汇款是否冲账退回，卖家确认收到款后，自动充值CNT。如超过24小时未收到币，请向客服反馈解决。', NULL, '2018-2-3 15:59:49');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (97, '充币多久时间到账？', '虚拟币转入需要经过网络确认后3个确认到账，需要备注的币种需填写好备注或MEMO，如未正确填写将无法自动到账，请联系客服。', NULL, '2018-2-3 16:00:24');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (97, '提币多久时间到账？', '提币需经过网络确认，一般15-45分钟到账，部分币种（BTC、SC等）到账时间会较长，如遇网络拥堵，提币到账时间会更长。', NULL, '2018-2-3 16:00:43');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (96, '为什么显示资产被冻结？', '显示资产冻结，一般是有未完成交易委托，交易成功或取消交易委托就不会显示冻结状态。如有提现或提币也会冻结相应的资产，撤销或提取成功就不会显示冻结状态。', NULL, '2018-2-3 16:01:20');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (96, '交易手续费怎么计算？', '交易手续费率固定为0.1%，只有交易成功的部分才会收取，未交易成功的部分或撤销挂单不收取费用，如对CNT交易，买入收取币的0.1%作为手续费，卖出收取CNT的0.1%作为手续费。', NULL, '2018-2-3 16:01:38');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (99, '如何修改登录密码及交易密码？', '可点击右上角用户ID——进入“会员中心”页面——选择“密码修改”——根据页面提示填入相关信息——点击“提交”即可。', NULL, '2018-2-3 16:02:12');
INSERT INTO `article` (`type_id`, `title`, `contents`, `image`, `publishtime`) VALUES (99, '忘记登录密码怎么办？', '点击页面右上角登录——进入“会员登录”页面——选择登录按钮下的“忘记密码”——根据页面提示填入相关信息——点击“提交”即可。', NULL, '2018-2-3 16:02:24');
