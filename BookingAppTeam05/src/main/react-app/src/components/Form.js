import React, {Component} from 'react';
import { Card, CardContent, Typography} from '@material-ui/core';

function ShowCottageProfile() {

    return (
        <Card>
            <CardContent>
                <Formik initialValues={{
                    cottageName:'',
                    address:'',
                    city:'',
                    zipCode:'',
                    country:'',
                    promoDescription:'',
                    numOfRooms:0,
                    rulesOfConduct:{},
                    pricePerNightPerPerson:0

                }}
                >
                    <Form>
                        <Field name ="firstName"/>
                    </Form>
                </Formik>
            </CardContent>
        </Card>
    );
}

export default ShowCottageProfile;