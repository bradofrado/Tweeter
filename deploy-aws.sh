#!/bin/bash
arr=(
        "getfollowing"
        "getfollowers"
        "getstory"
        "login"
        "follow"
	"getfeed"
	"getfollowerscount"
	"getfollowingcount"
	"getuser"
	"isfollower"
	"logout"
	"poststatus"
	"register"
	"unfollow"
    )
arr1=("poststatus"
"postupdatefeed"
"updatefeeds")
for FUNCTION_NAME in "${arr1[@]}"
do
  aws lambda update-function-code --function-name $FUNCTION_NAME --zip-file fileb://"c:\Users\brado\Documents\BYU\CS340\tweeter\server\build\libs\server-all.jar"
done
