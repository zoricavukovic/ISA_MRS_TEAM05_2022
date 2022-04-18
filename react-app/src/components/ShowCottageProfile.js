import React, {Component} from 'react';
import ImageCard from "./ImageCard";

function ShowCottageProfile(props) {

    return (
        <div>
            <ImageCard cottageId = {props.history.location.state.cottageId}/>
        </div>
    );
}

export default ShowCottageProfile;