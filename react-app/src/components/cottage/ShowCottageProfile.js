import React from 'react';
import ImageCard from "../cottage/ImageCard";
import { useEffect } from "react";


function ShowCottageProfile(props) {

    useEffect(() => {
        if (props.history.location.state === undefined || props.history.location.state === null){
            return <div>Do not allowed to go to this page. Try again!</div>
        }
    }, [])
    return (
        <div>
            <ImageCard cottageId = {props.history.location.state.bookingEntityId}/>
        </div>
    );
}

export default ShowCottageProfile;