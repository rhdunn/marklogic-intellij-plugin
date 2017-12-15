let $query := "$QUERY_STRING"
let $vars := ()
let $options := $OPTIONS
return try { $FUNCTION } catch ($e) { $e }
