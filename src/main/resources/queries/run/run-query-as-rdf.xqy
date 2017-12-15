import module namespace sem = "http://marklogic.com/semantics" at "/MarkLogic/semantics.xqy";
let $query := "$QUERY_STRING"
let $vars := ()
let $options := $OPTIONS
return try {
  let $ret     := $FUNCTION
  let $triples := for $item in $ret where $item instance of sem:triple return $item
  let $other   := for $item in $ret where not($item instance of sem:triple) return $item
  return if (count($triples) > 0) then
    let $fmt := sem:rdf-serialize($triples, "$TRIPLE_FORMAT")
    let $_ := xdmp:add-response-header("X-Content-Type", "$CONTENT_TYPE")
    return ($fmt, $other)
  else
    $ret
} catch ($e) { $e }
