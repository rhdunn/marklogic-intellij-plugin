import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy";

let $config := admin:get-configuration()
for $groupId in admin:get-group-ids($config)
  let $groupName := admin:group-get-name($config, $groupId)
  for $appServerId in admin:group-get-appserver-ids($config, $groupId)
    let $appServerName := admin:appserver-get-name($config, $appServerId)
    let $port := admin:appserver-get-port($config, $appServerId)
    let $type := admin:appserver-get-type($config, $appServerId)
    return ($groupName, $appServerName, $type, $port)
